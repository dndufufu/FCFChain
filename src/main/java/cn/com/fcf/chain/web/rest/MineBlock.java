package cn.com.fcf.chain.web.rest;

import cn.com.fcf.chain.domain.Block;
import cn.com.fcf.chain.repository.BlockRepository;
import cn.com.fcf.chain.service.BlockService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class MineBlock {

    private static final BlockService blockService = new BlockService();
    public static int difficulty = 5;
    public static List<Block> blockchain = new ArrayList<>();

    @Autowired
    private BlockRepository blockRepository;

    @Scheduled(cron = "0 */1 * * * ?")
    public void generateBlock() {
        Block block = new Block();
        String previousHash = blockchain.get(blockchain.size() - 1).getHash();
        block = blockService.createBlock(previousHash);
        blockService.mineBlock(difficulty, block);

        if (isChainValid()) {
            blockchain.add(block);
            blockRepository.save(block);
        } else {
            System.out.println("chain is inValid");
            return;
        }
        System.out.println(blockchain.size() + "****" + block.getHash());
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (
                !currentBlock
                    .getHash()
                    .equals(
                        blockService.calculateHash(
                            currentBlock.getPreviousHash(),
                            currentBlock.getMerkleRoot(),
                            currentBlock.getTimestamp(),
                            currentBlock.getNonce()
                        )
                    )
            ) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.getHash().substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
