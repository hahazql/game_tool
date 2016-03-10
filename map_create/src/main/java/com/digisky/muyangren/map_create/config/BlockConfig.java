package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zql on 16/2/29.
 *
 * @className BlockConfig
 * @classUse
 */
public class BlockConfig extends BaseConfig<BlockObjectConfig> {

    @Override
    public Collection<BlockObjectConfig> readConfig() {
        return new ArrayList<BlockObjectConfig>();
    }
}
