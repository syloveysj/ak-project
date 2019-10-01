import {NgModule} from '@angular/core';
import {TimePipe} from '@pipe/time.pipe';
import {TimesPipe} from '@pipe/times.pipe';
import {TextSubstringPipe} from '@pipe/textSubstring.pipe';
import {RoundPipe} from '@pipe/round.pipe';
import {RangePipe} from '@pipe/range.pipe';
import {NumPlusPipe} from '@pipe/num-plus.pipe';
import {NumColorPipe} from '@pipe/num-color.pipe';
import {SumPipe} from '@pipe/sum.pipe';
import {NullPipe} from '@pipe/null.pipe';
import {IconPipe} from '@pipe/icon.pipe';

const pipes = [
    TimePipe,
    TimesPipe,
    TextSubstringPipe,
    RoundPipe,
    RangePipe,
    NumPlusPipe,
    SumPipe,
    NumColorPipe,
    NullPipe,
    IconPipe,
];

@NgModule({
    declarations: [
        ...pipes
    ],
    exports: [
        ...pipes
    ]
})
export class PipeModule {
}
