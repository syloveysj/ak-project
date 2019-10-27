import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, Renderer2} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from "@config/config";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {BaseComponent} from "@shared/base-class/base.component";
import {ToolService} from "@core/utils/tool.service";
import {UUID} from "angular2-uuid";
import {InterfacesService} from "@service/http/interfaces.service";
import {Menu} from "@model/common";
import {finalize} from "rxjs/operators";

@Component({
    selector: 'app-apis-portfolio-node',
    templateUrl: './apis-portfolio-node.component.html',
    styles: [`
    `]
})
export class ApisPortfolioNodeComponent extends BaseComponent implements OnInit {

    @Output() update = new EventEmitter<any>();

    constructor(public baseService: BaseService,
        public rd: Renderer2,
        public modalService: NzModalService,
        public nzMessageService: NzMessageService,
        public cdr: ChangeDetectorRef,
        public toolService: ToolService,
        public fb: FormBuilder,
        public interfacesService: InterfacesService,
        public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

    bean: any;
    // 0:查看，1:编辑
    state = 0;
    loading = true;
    form: FormGroup;
    siderData: { serverId: string, menuId:string };
    menus: Menu[];

    ngOnInit(): void {
        this.form = this.fb.group({
            id: [null, [Validators.required]],
            pid: [null, [Validators.required]],
            serviceId: [null, [Validators.required]],
            alias: [null, [Validators.required]],
            memo: [null]
        });
    }

    initBean() {
        if(this.siderData === null) return;

        if(this.siderData.menuId === 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb') {
            this.bean = {
                id: 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
                pid: 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
                serviceId: this.siderData.serverId,
                alias: '待分类',
                memo: '',
                _alias: '根节点'
            };
            this.showBean();
        } else {
            this.interfacesService.getApisClassify(this.siderData.menuId).pipe(
                finalize(() => this.loading = false)
            ).subscribe(
                (res) => {
                    this.bean = res;
                    this.showBean();
                }
            );
        }
    }

    showBean() {
        this.form.get("id").setValue(this.bean.id);
        this.form.get("pid").setValue(this.bean.pid);
        this.form.get("serviceId").setValue(this.bean.serviceId);
        this.form.get("alias").setValue(this.bean.alias);
        this.form.get("memo").setValue(this.bean.memo);
        console.log(this.menus)
        if(this.menus) {
            const list = this.menus.filter(item => item.id === this.bean.pid);
            this.bean._alias = list.length>0 ? list[0].title : '根节点';
        }
    }

    setData(data: { serverId: string, menuId:string }) {
        if(this.bean && this.bean.serviceId === data.serverId && this.bean.id === data.menuId) return;
        this.state = 0;
        this.siderData = data;
        this.initBean();
    }

    setMenus(menus: Menu[]) {
        this.menus = menus;
    }

    saveData() {
        this.loading = true;
        this.interfacesService.updateApisClassify(this.bean.id, this.form.value).pipe(
            finalize(() => this.loading = false)
        ).subscribe(
            () => {
                this.state = 0;
                this.initBean();
                this.update.emit(true);
            }
        );
    }
}
