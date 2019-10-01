import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
    name: 'numPlus',
})
export class NumPlusPipe implements PipeTransform {
    transform(value: number, max: number): string {
        return value > max ? String(max) + '+' : String(value);
    }
}
