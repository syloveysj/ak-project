import {
    Component,
    EventEmitter, forwardRef,
    Inject,
    Input,
    OnChanges,
    Optional,
    Output, Renderer2, RendererFactory2,
    SimpleChanges
} from '@angular/core';
import {InputBoolean, NzTreeNodeOptions} from 'ng-zorro-antd';
import {DOCUMENT} from "@angular/common";
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from "@angular/forms";
import {isNotEmpty} from "@core/utils/string.util";

@Component({
    selector: 'app-application-tree-select',
    template: `
        <nz-tree-select
            [ngStyle]="style"
            [nzDropdownStyle]="{ 'max-height': '400px' }"
            [nzDropdownMatchSelectWidth]="false"
            [nzNodes]="nodes || []"
            [nzSize]="size"
            nzShowSearch
            [nzAllowClear]="allowClear"
            [nzPlaceHolder]="'请选择应用'"
            (ngModelChange)="getKey($event)"
            [(ngModel)]="value"
            (nzOpenChange)="openChange($event)"
        >
        </nz-tree-select>
    `,
    styles: [`
        :host ::ng-deep .ant-select-tree li {
            margin: 4px 0;
        }

        :host ::ng-deep.ant-select-tree li span.ant-select-tree-switcher, .ant-select-tree li span.ant-select-tree-iconEle {
            margin: -3px;
        }
        
        :host ::ng-deep  nz-tree.only-market > ul > nz-tree-node > li > span.ant-select-tree-node-content-wrapper:hover {
            background: unset !important;
            cursor: unset;
        }
    `],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => ApplicationTreeSelectComponent),
            multi: true
        }
    ],
})
export class ApplicationTreeSelectComponent implements OnChanges, ControlValueAccessor {

    value = null;
    prevValue = this.value;
    @Input() initServerId = null;
    @Input() nodes: NzTreeNodeOptions[];
    @Input() size = 'default';
    @Input() style: any = { 'width': '250px' };
    @Input() loading: boolean;
    @Input() @InputBoolean() allowClear = true; // 是否允许清空，如果允许则表示可以选择所有。不能则自动定位到第一个分类或第一个分类的第一个服务。
    @Output() serverIdChange = new EventEmitter<{ serverId: string }>();

    onChange: (_: any) => void = () => null;
    onTouched: () => void = () => null;

    private renderer: Renderer2;

    constructor(@Optional() @Inject(DOCUMENT) private document: Document,
                private rendererFactory: RendererFactory2) {
        this.renderer = rendererFactory.createRenderer(null, null);
    }

    openChange(open: boolean) {
        if (open) {
            setTimeout(() => {
                this.document.querySelectorAll('nz-tree > ul > nz-tree-node > li').forEach(node => {
                    node.addEventListener('click', (event) => {
                        (node.firstElementChild as HTMLSpanElement)!.click();
                        event.stopPropagation();
                        event.preventDefault();
                    })
                });
                this.document.querySelectorAll('nz-tree > ul > nz-tree-node > li > span.ant-select-tree-node-content-wrapper').forEach(node => {
                    this.renderer.setStyle(node, 'background-color', '#fff');
                })
            })
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.nodes && this.translateNodes(this.nodes);
    }

    reset() {
        this.translateNodes(this.nodes);
    }

    clear() {
        this.value = this.prevValue = null;
        this.getKey(null);
    }

    private translateNodes(nodes: NzTreeNodeOptions[]) {
        if (!this.allowClear) {
            if (isNotEmpty(this.initServerId)) {
                this.prevValue = this.value = this.initServerId;
            } else {
                if('children' in nodes[0] && nodes[0].children.length>0) {
                    this.prevValue = this.value = nodes[0].children[0].key;
                } else {
                    this.prevValue = this.value = null;
                }
            }
        }
        this.getKey(this.value);
    }

    getKey(key: string) {
        if (!this.allowClear) {
            if (key == null) {
                setTimeout(() => {
                    this.value = this.prevValue;
                }, 100);
                return;
            } else {
                this.prevValue = key;
            }
        }

        const value = {serverId: key || null};
        this.serverIdChange.emit(value);
    }

    registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    setDisabledState(isDisabled: boolean): void {
    }

    writeValue(obj: any): void {
    }

}
