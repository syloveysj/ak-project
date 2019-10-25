import {Component, OnInit, ViewChild} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Config} from "@config/config";
import {BaseService} from "@service/http/base.service";
import {GatewayService} from "@service/http/gateway.service";

@Component({
    selector: 'app-apis-import',
    templateUrl: './apis-import.component.html',
    styles: [`
    `],
})
export class ApisImportComponent implements OnInit {
    @ViewChild('onePanelChild') onePanel;
    @ViewChild('twoPanelChild') twoPanel;

    constructor(public fb: FormBuilder,
                public config: Config,
                private nzMessageService: NzMessageService,
                public modalService: NzModalService,
                public baseService: BaseService,
                public gatewayService: GatewayService) {
    }

    form: FormGroup;
    loading = false;
    current = 0;
    apisBean: {serverId: string, rows: any[], jsonText: string};

    ngOnInit(): void {
        this.form = this.fb.group({
            routeName: ['', [Validators.required]],
        });
    }

    getFromValues(): any {
        if(this.current === 0) {
            return this.onePanel.getFromValues();
        } else if(this.current === 1) {
            return {
                serverId: this.apisBean.serverId,
                rows: this.twoPanel.getFromValues(),
                jsonText: this.apisBean.jsonText
            };
        }
        return null;
    }

    setApisBean(data: any) {
        this.apisBean = data;
    }

}
