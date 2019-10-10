import {Pipe, PipeTransform} from '@angular/core';

/**
 * @description: 默认截取50个字符，可自行设置
 * @example:
 * {{item?.content | textSubstring:0:100}} // 截取0到100个字符
 */
@Pipe({
    name: 'textSubstring',
})
export class TextSubstringPipe implements PipeTransform {
    transform(value: string, startIndex = 0, length = 50, ellipsis = '...'): string {
        if (value && value.length > length) {
            return value.slice(startIndex, length) + ellipsis;
        }
        return value;
    }
}
