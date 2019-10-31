import {Component} from '@angular/core';


@Component({
    selector: 'app-auth',
    template: `
        <div>
            <div class="auth-header clearFix">
                <div class="auth-header-wrapper">
                    <div class="logo fl">
                        <p class="zn-text">
                            <!--<img src="../../../assets/images/logo.png" alt="云管理平台" style="width: 234px;">-->
                        </p>
                        <!--<p class="en-text">云管理平台</p>-->
                    </div>

                </div>
            </div>
            <router-outlet></router-outlet>
        </div>
    `,
    styles: [`
        .auth-header {
            height: 120px;
            background: #fff;
        }

        .auth-header-wrapper {
            margin-left: 10%;
        }

        .zn-text {
            font-size: 30px;
            color: #64a0ff;
            letter-spacing: 4px;
            margin: 24px 0 0 0;
        }

        .en-text {
            font-size: 14px;
            color: #64a0ff;
            text-indent: 64px;
        }

    `]
})

export class AuthComponent {

}
