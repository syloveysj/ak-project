import {Pipe, PipeTransform} from '@angular/core';
import {isBlank} from '@core/utils/tool.util';

@Pipe({
    name: 'null',
})
export class NullPipe implements PipeTransform {
    transform(value: number, result: any = ''): any {
        return isBlank(value) ? result : value;
    }
}
