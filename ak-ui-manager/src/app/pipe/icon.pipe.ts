/**
 * 默认展示
 */
import {Pipe, PipeTransform} from '@angular/core';

@Pipe
({
    name: 'iconPipe',
})
export class IconPipe implements PipeTransform {
    transform(value: string): string {
        let str = value.split('-')[1];
        switch (str) {
            case 'add':
                str = 'plus';
                break;
            case 'del':
                str = 'delete';
                break;
            case 'list':
                str = 'ordered-list';
                break;
            case 'ok':
                str = 'check';
                break;
            case 'company':
                str = 'folder';
                break;
            case 'view':
                str = 'zoom-in';
                break;
            case 'back':
                str = 'undo';
                break;
            case 'man':
                str = 'user';
                break;
        }
        return str ? str : value;
    }
}
