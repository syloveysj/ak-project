import {Pipe, PipeTransform} from '@angular/core';
import {isBlank} from '@core/utils/tool.util';

@Pipe({
    name: 'sum',
})
export class SumPipe implements PipeTransform {
    transform(values: any[], objName: string, isAmount?: boolean): any {
        if (!isBlank(values)) {
            if (isAmount) {
                return values.map(item => Number(item[objName])).reduce((e1, e2) => e1 + e2).toFixed(4);
            }
            return values.map(item => item[objName]).reduce((e1, e2) => e1 + e2);
        } else {
            return 0;
        }
    }
}
