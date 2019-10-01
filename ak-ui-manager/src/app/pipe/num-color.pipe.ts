import {Pipe, PipeTransform} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';

@Pipe({
    name: 'numColor',
})
export class NumColorPipe implements PipeTransform {

    constructor(private sanitizer: DomSanitizer) {
    }

    transform(value: number | string, icon: string = '', colors: any = {'>0': 'green', '=0': 'unset', '<0': 'red'}) {
        let num: any = value;
        if (typeof value === 'string') {
            num = Number((num as string).replace(',', ''));
        }
        let innerHtml;
        if (num > 0) {
            innerHtml = `<span style="color: ${colors['>0']}">${icon}${value}</span>`;
        } else if (num == 0) {
            innerHtml = `<span style="color: ${colors['=0']}">${icon}${value}</span>`;
        } else {
            innerHtml = `<span style="color: ${colors['<0']}">${icon}${value}</span>`;
        }
        return this.sanitizer.bypassSecurityTrustHtml(innerHtml);
    }
}
