package cn.com.fcf.chain.service;

import cn.com.fcf.chain.domain.Block;
import cn.com.fcf.chain.domain.Transaction;
import cn.com.fcf.chain.repository.BlockRepository;
import cn.com.fcf.chain.util.StringUtil;
import cn.com.fcf.chain.web.rest.MineBlock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlockService {

    @Autowired
    private BlockRepository blockRepository;

    /**
     * 生成创世区块
     */
    @PostConstruct
    private void generateGenesisBlock() {
        Block block = createBlock("0");
        mineBlock(MineBlock.difficulty, block);
        MineBlock.blockchain.add(block);
        blockRepository.save(block);
        System.out.println(MineBlock.blockchain.size() + "****" + block.getHash());
    }

    @Transactional
    public Block createBlock(String previousHash) {
        Block block = new Block();
        block.setPreviousHash(previousHash);
        block.timestamp(new Date().toInstant());
        block.setHash(calculateHash(block.getPreviousHash(), block.getMerkleRoot(), block.getTimestamp(), block.getNonce()));
        return block;
    }

    public String calculateHash(String previousHash, String merkleRoot, Instant timestamp, Integer nonce) {
        String calculatedHash = StringUtil.applySha256(previousHash + merkleRoot + timestamp + nonce);
        return calculatedHash;
    }

    @Transactional
    public void mineBlock(int difficulty, Block block) {
        block.setMerkleRoot(StringUtil.getMerkleRoot((ArrayList<Transaction>) block.getTransactions()));
        String target = StringUtil.getDifficultyString(difficulty);
        while (!block.getHash().substring(0, difficulty).equals(target)) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(calculateHash(block.getPreviousHash(), block.getMerkleRoot(), block.getTimestamp(), block.getNonce()));
        }
        //        System.out.println("Block Mined!!!!" + block.getHash());
        //        return block;
    }
}
