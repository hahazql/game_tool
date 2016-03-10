package com.digisky.muyangren.map_create.config;/**
 * Created by zql on 16/2/29.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zql on 16/2/29.
 *
 * @className TemplateConfig
 * @classUse
 */
public class TemplateConfig extends BaseConfig<TemplateObjectConfig> {
    @Override
    public Collection<TemplateObjectConfig> readConfig() {
        List<TemplateObjectConfig> list = new ArrayList<TemplateObjectConfig>();
        TemplateObjectConfig config = new TemplateObjectConfig();
        config.setId(0);
        config.addConfig(new UnitCreateObjectConfig(1, UnitType.forest, 1, 5, ModelType.forest), 230)
                .addConfig(new UnitCreateObjectConfig(3, UnitType.mountain, 2, 15, ModelType.mountain_2), 150)
                .addConfig(new UnitCreateObjectConfig(4, UnitType.forest, 3, 20, ModelType.forest_3), 40)
                .addConfig(new UnitCreateObjectConfig(5, UnitType.forest, 2, 14, ModelType.forest_2), 70);
        list.add(config);
        return list;
    }
}
