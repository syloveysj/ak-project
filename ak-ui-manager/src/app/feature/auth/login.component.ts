import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';
import {environment} from '@environments/environment';
import {BaseService} from '@service/http/base.service';
import {Store} from '@ngrx/store';
import {ToolService} from "@core/utils/tool.service";
import * as fromRoot from "@store/reducers";
import {Oauth2Service} from "@service/http/oauth2.service";

@Component({
    selector: 'app-login',
    template: `
        <div class="wrapper">
            <div class="login-newbg"
                 style="background-image: url('/assets/images/login-bg.png'); background-size: 1300px; height: 600px;"></div>
            <div class="center clearFix">
                <form nz-form [formGroup]="form" class="login-form fr">
                    <p class="welcome">欢迎登录</p>
                    <nz-form-item>
                        <nz-form-control><!--[nzErrorTip]="usernameErrorTpl"-->
                            <nz-input-group [nzPrefix]="prefixUser">
                                <input type="text" class="rs30" nz-input formControlName="username" placeholder="账号">
                            </nz-input-group>
                            <nz-form-explain *ngIf="form.get('username').dirty && form.get('username').errors">
                                请输入您的用户名！
                            </nz-form-explain>
                        </nz-form-control>
                    </nz-form-item>
                    <nz-form-item>
                        <nz-form-control><!--[nzErrorTip]="passwordErrorTpl"-->
                            <nz-input-group [nzPrefix]="prefixLock">
                                <input type="password" class="rs30" nz-input formControlName="password"
                                       placeholder="密码">
                            </nz-input-group>
                            <nz-form-explain *ngIf="form.get('password').dirty && form.get('password').errors">
                                请输入您的密码！
                            </nz-form-explain>
                        </nz-form-control>
                    </nz-form-item>
                    <nz-form-item>
                        <nz-form-control>
                            <button
                                appDebounceClick
                                (debounceClick)="submitForm()"
                                nz-button class="rs30 login-form-button" [nzType]="'primary'" [nzLoading]="loading"
                                [disabled]="!form.valid">登录
                            </button>
                        </nz-form-control>
                    </nz-form-item>
                </form>
                <ng-template #prefixUser><i nz-icon type="user"></i></ng-template>
                <ng-template #prefixLock><i nz-icon type="lock"></i></ng-template>
            </div>
        </div>
    `,
    styles: [`
        .wrapper {
            background: #fcfdff;
            min-height: 840px;
            width: 100%;
            position: relative;
        }

        .center {
            position: relative;
            z-index: 999;
            padding: 50px 0;
        }

        .rs30 {
            border-radius: 30px;
        }

        .login-newbg {
            position: absolute;
            z-index: 9;
            top: 0;
            left: 0;
            background-size: cover;
            background: no-repeat 33% center;
            width: 100%;
        }

        .login-box-warp {
            padding: 10px 30px;
            position: absolute;
            top: 90px;
            right: 60px;
            width: 350px;
            z-index: 999;
            background: rgba(255, 255, 255, .9);
        }

        .welcome {
            font-size: 22px;
            color: #64a0ff;
            text-align: center;
        }

        .login-form {
            margin-top: 130px;
            width: 300px;
        }

        .login-form-button {
            width: 100%;
        }

    `]
})
export class LoginComponent implements OnInit {

    form: FormGroup;
    username: string = '';
    pwd: string = '';

    loading: boolean = false;

    constructor(private fb: FormBuilder,
                private router: Router,
                public nzMessageService: NzMessageService,
                public toolService: ToolService,
                public baseService: BaseService,
                private oauth2Service: Oauth2Service,
                private store$: Store<fromRoot.State>) {
    }

    ngOnInit(): void {
        if (environment.production) {
            this.username = '';
            this.pwd = '';
        } else {
            this.username = 'admin';
            this.pwd = '123456';
        }

        this.form = this.fb.group({
            username: [this.username, [Validators.required]],
            password: [this.pwd, [Validators.required]]
        });
    }

    submitForm(): void {
        for (const i in this.form.controls) {
            this.form.controls[i].markAsDirty();
            this.form.controls[i].updateValueAndValidity();
        }
        this.oauth2Service.obtainAccessToken(this.form.value);
    }

}
