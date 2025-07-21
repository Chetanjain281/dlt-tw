package com.dlt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Component
public class HashUtil {
    
    private static final String GENESIS_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
    private final ObjectMapper objectMapper;
    
    public HashUtil() {
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Calculate SHA-256 hash for the given data
     */
    public String calculateHash(String previousHash, Map<String, Object> data) {
        try {
            String dataJson = objectMapper.writeValueAsString(data);
            String input = previousHash + dataJson;
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating hash", e);
        }
    }
    
    /**
     * Get the genesis hash for the first block
     */
    public String getGenesisHash() {
        return GENESIS_HASH;
    }
    
    /**
     * Validate hash chain integrity
     */
    public boolean validateHash(String previousHash, Map<String, Object> data, String expectedHash) {
        String calculatedHash = calculateHash(previousHash, data);
        return calculatedHash.equals(expectedHash);
    }
}
