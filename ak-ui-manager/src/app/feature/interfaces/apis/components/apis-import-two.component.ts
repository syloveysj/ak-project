import {ChangeDetectorRef, Component, Input, OnInit, Renderer2} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder} from "@angular/forms";
import {BaseComponent} from "@shared/base-class/base.component";
import {ToolService} from "@core/utils/tool.service";
import {isNotEmpty} from "@core/utils/string.util";

@Component({
    selector: 'app-apis-import-two',
    templateUrl: './apis-import-two.component.html',
    styles: [`
        ._get { color: #0F6AB4; }
        ._post { color: #10A54A; }
        ._delete { color: #A41E22; }
        ._put { color: #C5862B; }
        ._head { color: #FFD20F; }
        ._patch { color: #D38042; }
        ._options { color: #0F6AB4; }
        ._trace { color: #0F6AB4; }
    `]
})
export class ApisImportTwoComponent extends BaseComponent implements OnInit {

    @Input() apiList: any[] = [];

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public cdr: ChangeDetectorRef,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public toolService: ToolService,
                public fb: FormBuilder) {
        super(baseService, rd, modalService, nzMessageService);
    }

    state = null;
    allCheckedNumber = 0;
    showApiList: any[] = [];
    keyword;

    ngOnInit(): void {
        this.showApiList = this.apiList;
        this.showApiList.sort((a, b) => {
            return a.state - b.state;
        })
    }

    getFromValues(): any {
        this.updateApiList();
        return this.apiList.filter(value => value.checked);
    }

    filterApiList(): void {
        this.updateApiList();
        if(this.state === null) {
            this.showApiList = this.apiList;
        } else {
            this.showApiList = this.apiList.filter(item => item.state === this.state);
        }
        if(isNotEmpty(this.keyword)) {
            this.showApiList = this.showApiList.filter(item => item.name.indexOf(this.keyword)>-1 || item.method.indexOf(this.keyword)>-1 || item.uri.indexOf(this.keyword)>-1);
        }
    }

    updateApiList(): void {
        this.showApiList.forEach(data => {
            const index = this.apiList.findIndex(item => {
                return data.name === item.name;
            });
            if(index > -1) this.apiList[index].checked = data.checked;
        });
    }

    checkAll(value: boolean): void {
        this.displayData.forEach(data => (data.state !== 2) && (data.checked = value));
        this.showApiList.forEach(data => {
            const index = this.displayData.findIndex(item => {
                return data.name === item.name;
            });
            if(index > -1 && data.state !== 2) data.checked = value;
        });
        this.refreshStatus();
    }

    checkedChange(item: any): void {
        this.showApiList.forEach(data => (data.name === item.name) && (data.checked = item.checked));
        this.refreshStatus();
    }

    currentPageDataChange($event: any[]): void {
        this.displayData = $event;
        this.refreshStatus();
    }

    refreshStatus(): void {
        this.allCheckedNumber = this.apiList.filter(value => value.checked).length;
        super.refreshStatus();
    }

    clearChecked() {
        this.displayData.forEach(data => data.checked = false);
        this.apiList.forEach(data => data.checked = false);
        this.refreshStatus();
    }

    checkedAll() {
        this.displayData.forEach(data => (data.state !== 2) && (data.checked = true));
        this.apiList.forEach(data => (data.state !== 2) && (data.checked = true));
        this.refreshStatus();
    }

    deleteItem(item: any) {
        const index1 = this.showApiList.findIndex(data => {
            return data.name === item.name;
        });
        if(index1 > -1) this.showApiList.splice(index1, 1);

        const index2 = this.apiList.findIndex(data => {
            return data.name === item.name;
        });
        if(index2 > -1) this.apiList.splice(index2, 1);
        this.showApiList = [...this.showApiList];
    }
}
