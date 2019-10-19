import {ChangeDetectorRef, Component, Input, OnInit, Renderer2} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {GatewayService} from "@service/http/gateway.service";
import {Config} from "@config/config";
import {FormBuilder} from "@angular/forms";
import {BaseComponent} from "@shared/base-class/base.component";
import {ToolService} from "@core/utils/tool.service";
import {UUID} from "angular2-uuid";
import {isEmpty, isNotEmpty} from "@core/utils/string.util";
import {RouterPluginsEditComponent} from "@feature/gateway/advanced-router/components/router-plugins-edit.component";

@Component({
    selector: 'app-apis-server-setting',
    templateUrl: './apis-server-setting.component.html',
    styles: [`
    `]
})
export class ApisServerSettingComponent extends BaseComponent implements OnInit {

    @Input() bean: any;

    targets: any[] = [];
    editCache: { [key: string]: any } = {};
    editing = false;

    pluginsList: any[] = [{name:'key-auth', enabled: true, config: ''}];

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public cdr: ChangeDetectorRef,
                public toolService: ToolService,
                public fb: FormBuilder,
                public gatewayService: GatewayService,
                public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

    ngOnInit(): void {
    }

    initTaggets() {
        // 编辑时初始化
        if (this.bean != null) {
            this.gatewayService.getUpstreamTargetList(this.bean.id).subscribe(
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
            this.gatewayService.addTarget(this.bean.id, {target:item.ip + ':' + (isNotEmpty(item.port) ? item.port : '80'), weight: isNotEmpty(item.weight) ? item.weight : '100'}).subscribe(
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
                    this.gatewayService.removeTargetList(this.bean.id, ids).subscribe(
                        (res) => {
                            this.initTaggets();
                        }
                    );
                }
            } else {
                this.gatewayService.removeTarget(this.bean.id, obj.id).subscribe(
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
