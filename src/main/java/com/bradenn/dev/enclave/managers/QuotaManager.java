package com.bradenn.dev.enclave.managers;

import com.bradenn.dev.enclave.Main;
import com.bradenn.dev.enclave.models.EnclaveModel;

import java.util.UUID;

public class QuotaManager {

    private final EnclaveModel enclaveModel;

    public QuotaManager(UUID enclave) {
        enclaveModel = new EnclaveModel(enclave);
    }

    /**
     * Get the default allotment of chunks from config.
     * @return number of chunks allotted by default.
     */
    private int getDefaultChunkQuota() {
        return Main.plugin.getConfig().getInt("quotas.default.chunks");
    }

    /**
     * Determine if claiming one more chunk would exceed the claim limit.
     * @return number of chunks allotted to an enclave.
     */
    public int getChunkQuota() {
        int customQuota = enclaveModel.getChunkQuota();
        System.out.println(customQuota);
        int defaultQuota = getDefaultChunkQuota();
        return Math.max(customQuota, defaultQuota);
    }

    /**
     * Get the current number of claimed chunks
     * @return number of chunks claimed by an enclave.
     */
    public int getChunksClaimed() {
        return enclaveModel.getChunkCount();
    }

    /**
     * Determine if claiming one more chunk would exceed the claim limit.
     * @return true if the enclave has too many chunk claims.
     */
    public boolean willExceedChunkQuota() {
        int chunkCount = enclaveModel.getChunkCount() + 1;
        return chunkCount > getChunkQuota();
    }


}
