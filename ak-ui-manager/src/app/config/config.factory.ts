import {ConfigDev} from './config-dev';
import {ConfigProd} from './config-prod';

import {environment} from '@environments/environment';

export let configFactory = () => { // 根据环境决定配置。
    if (environment.production) {
        return new ConfigProd();
    } else {
        return new ConfigDev();
    }
};
