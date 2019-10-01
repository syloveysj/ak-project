import {Pipe, PipeTransform} from '@angular/core';
import {format} from 'date-fns';

@Pipe({
    name: 'time',
})
export class TimePipe implements PipeTransform {
    transform(value: number): string {
        if (!value) {
            return '-';
        }
        return format(value, 'YYYY-MM-DD HH:mm:ss');
    }
}
