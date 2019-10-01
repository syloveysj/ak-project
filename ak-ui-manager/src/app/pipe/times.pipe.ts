import {Pipe, PipeTransform} from '@angular/core';
import {toNumber} from 'ng-zorro-antd';
import {isFunction} from 'util';

const MAX_SAFE_INTEGER = 9007199254740991;
const MAX_ARRAY_LENGTH = 4294967295;

@Pipe({
    name: 'times'
})
export class TimesPipe implements PipeTransform {
    transform(value: any, args?: (index: number) => void): any {
        value = toNumber(value);
        if (value < 1 || value > MAX_SAFE_INTEGER) {
            return [];
        }

        let index = MAX_ARRAY_LENGTH;
        const length = Math.min(value, MAX_ARRAY_LENGTH);

        if (!isFunction(args)) {
            args = () => {
            };
        }
        value -= MAX_ARRAY_LENGTH;

        const result = baseTimes(length, args);
        while (++index < value) {
            args(index);
        }
        return result;
    }
}

function baseTimes(n, fn): any {
    let index = -1;
    const result = Array(n);

    while (++index < n) {
        result[index] = fn(index);
    }
    return result;
}
