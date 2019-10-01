import {Pipe, PipeTransform} from '@angular/core';
import {isBlank} from '@core/utils/tool.util';

@Pipe({
    name: 'round',
})
export class RoundPipe implements PipeTransform {
    transform(value: number, round: number = 2): string | number {
        return isBlank(value) ? value : value.toFixed(2).replace(/\.00$|\.0$/, '');
    }
}
