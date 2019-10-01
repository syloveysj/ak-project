import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'range',
})
export class RangePipe implements PipeTransform {
    transform(to: number, from: number = 0, step: number = 1): number[] {
        const result = [];
        for (let from = 0; from < to; from += step) {
            result.push(from);
        }
        return result;
    }
}
