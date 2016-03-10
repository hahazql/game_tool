package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zql on 16/2/29.
 *
 * @className RetainConfig
 * @classUse
 */
public class RetainConfig extends BaseConfig<RetainObjectConfig> {
    @Override
    public Collection<RetainObjectConfig> readConfig() {
        return new ArrayList<RetainObjectConfig>();
    }
}
