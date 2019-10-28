import {ChangeDetectorRef, Component, EventEmitter, OnInit, Output, Renderer2} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from "@config/config";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {BaseComponent} from "@shared/base-class/base.component";
import {ToolService} from "@core/utils/tool.service";
import {UUID} from "angular2-uuid";
import {isEmpty, isNotEmpty} from "@core/utils/string.util";
import {RouterPluginsEditComponent} from "@feature/gateway/advanced-router/components/router-plugins-edit.component";
import {finalize} from "rxjs/operators";
import {InterfacesService} from "@service/http/interfaces.service";
import * as fromRoot from "@store/reducers";
import {Store} from "@ngrx/store";
import {Option} from "@model/common";
import * as ConstantsActions from "@store/actions/constants.actions";

@Component({
    selector: 'app-apis-server-setting',
    templateUrl: './apis-server-setting.component.html',
    styles: [`
    `]
})
export class ApisServerSettingComponent extends BaseComponent implements OnInit {

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public cdr: ChangeDetectorRef,
                public toolService: ToolService,
                public fb: FormBuilder,
                private store$: Store<fromRoot.State>,
                public interfacesService: InterfacesService,
                public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

    bean: any;
    applicationTypes: Option[] = [];
    // 0:查看，1:编辑
    state = 0;
    loading = true;
    form: FormGroup;
    siderData: { serverId: string, menuId:string };
    targets: any[] = [];
    editCache: { [key: string]: any } = {};
    editing = false;

    pluginsList: any[] = [{name:'key-auth', enabled: true, config: ''}];

    ngOnInit(): void {
        this.form = this.fb.group({
            typeId: [null, [Validators.required]],
            alias: [null, [Validators.required]],
            memo: [null],
            connectTimeout: [null, [Validators.required]],
            readTimeout: [null, [Validators.required]],
            writeTimeout: [null, [Validators.required]],
            protocol: [null, [Validators.required]],
            path: [null],
            port: [null, [Validators.required]]
        });

        this.applicationTypes$ = this.store$.select(fromRoot.getApplicationTypes);
        this.applicationTypes$.subscribe(data => {
            this.applicationTypes = data;
        });
    }

    initBean(flag:boolean = false) {
        this.interfacesService.getService(this.siderData.serverId).pipe(
            finalize(() => this.loading = false)
        ).subscribe(
            (res) => {
                this.bean = res;
                this.applicationTypes.forEach(item => {
                   if(this.bean.typeId === item.id) {
                       this.bean.typeName = item.typeName;
                   }
                });
                this.showBean();
                if(flag) this.initTaggets();
            }
        );
    }

    showBean() {
        this.form.get("typeId").setValue(this.bean.typeId);
        this.form.get("alias").setValue(this.bean.alias);
        this.form.get("memo").setValue(this.bean.memo);
        this.form.get("connectTimeout").setValue(this.bean.connectTimeout);
        this.form.get("readTimeout").setValue(this.bean.readTimeout);
        this.form.get("writeTimeout").setValue(this.bean.writeTimeout);
        this.form.get("protocol").setValue(this.bean.protocol);
        this.form.get("path").setValue(this.bean.path);
        this.form.get("port").setValue(this.bean.port);
    }

    setData(data: { serverId: string, menuId:string }) {
        this.siderData = data;
        if(this.bean && this.bean.id === data.serverId) return;
        this.state = 0;
        this.initBean(true);
    }

    saveData() {
        this.loading = true;
        const params = this.form.value;
        params.id = this.bean.id;
        params.name = this.bean.name;
        params.host = this.bean.host;
        params.path = isEmpty(params.path) ? null : params.path;
        this.interfacesService.updateService(this.bean.id, params).pipe(
            finalize(() => this.loading = false)
        ).subscribe(
            () => {
                this.state = 0;
                this.initBean();
                this.store$.dispatch(new ConstantsActions.LoadServices());
            }
        );
    }

    initTaggets() {
        // 编辑时初始化
        if (this.bean != null) {
            this.interfacesService.getUpstreamTargetList(this.bean.host).subscribe(
                (res) => {
                    if(res !== null) {
                        this.targets = [];
                        res.forEach(item => {
                            const strs: string[] = item.target.split(':');
                            this.targets.push({
                                id: item.id,
                                ip: strs[0],
                                port: strs[1],
                                weight: item.weight,
                                //标记新增
                                flag: false
                            });
                        });
                    }
                    this.updateEditCache();
                }
            );
        } else {
            this.updateEditCache();
        }
    }

    updateEditCache(): void {
        this.editing = false;
        this.editCache = {};
        this.targets.forEach(item => {
            this.editCache[item.id] = {
                edit: false,
                data: { ...item }
            };
        });
    }

    startEdit(id: string): void {
        this.editCache[id].edit = true;
        this.editing = true;
    }

    cancelEdit(id: string): void {
        const index = this.targets.findIndex(item => item.id === id);
        if('flag' in this.targets[index] && this.bean != null) {
            //编辑状态下 -> 新增目标状态，取消移除
            this.targets.splice(index, 1);
            this.targets = [...this.targets];
            this.updateEditCache();
        } else {
            this.editCache[id] = {
                data: { ...this.targets[index] },
                edit: false
            };
            this.editing = false;
        }
    }

    addTarget() {
        const newId = UUID.UUID();
        this.targets.push({
            id: newId,
            ip: '',
            port: '8080',
            weight: '100',
            //标记新增
            flag: true
        });
        this.updateEditCache();
        this.editCache[newId].edit = true;
        this.editing = true;
        this.targets = [...this.targets];
    }

    saveTarget(id: string) {
        const index = this.targets.findIndex(item => item.id === id);
        Object.assign(this.targets[index], this.editCache[id].data);

        const item = this.targets[index];
        if (this.bean != null) {
            this.interfacesService.addTarget(this.bean.host, {target:item.ip + ':' + (isNotEmpty(item.port) ? item.port : '80'), weight: isNotEmpty(item.weight) ? item.weight : '100'}).subscribe(
                (res) => {
                    this.initTaggets();
                }
            );
        } else {
            this.targets = [...this.targets];
            this.updateEditCache();
        }
    }

    deleteTargets(obj:any = null) {
        if(this.bean != null) {
            if(obj === null) {
                let ids = "";
                this.targets.forEach(item => {
                    if(item.checked === true) {
                        ids += isEmpty(ids)? item.id : ',' + item.id;
                    }
                });
                if(isNotEmpty(ids)) {
                    this.interfacesService.removeTargetList(this.bean.host, ids).subscribe(
                        (res) => {
                            this.initTaggets();
                        }
                    );
                }
            } else {
                this.interfacesService.removeTarget(this.bean.host, obj.id).subscribe(
                    (res) => {
                        this.initTaggets();
                    }
                );
            }
        } else {
            if(obj === null) {
                this.targets = this.targets.filter(item => !item.checked);
            } else {
                const index = this.targets.findIndex(item => item.id === obj.id);
                this.targets.splice(index, 1);
                this.targets = [...this.targets];
            }
            this.updateEditCache();
        }
    }

    openPluginsWin(bean: any) {
        const modal = this.modalService.create({
            nzWrapClassName: 'vertical-center-modal large',
            nzTitle: '添加插件',
            nzMaskClosable: false,
            nzFooter: [
                {
                    label: '保存',
                    shape: 'primary',
                    disabled: (componentInstance) => {
                        return !componentInstance.form.valid;
                    },
                    onClick: (componentInstance) => {
                        console.log(componentInstance.form.value);
                        modal.destroy();
                    }
                },
                {
                    label: '取消',
                    shape: 'default',
                    onClick: () => {
                        modal.destroy();
                    }
                }
            ],
            nzContent: RouterPluginsEditComponent,
            nzComponentParams: {
                bean
            }
        });

        modal.afterClose.subscribe((result) => {
            if (result) {
            }
        });
    }

    deletePlug(bean: any) {
    }
}
