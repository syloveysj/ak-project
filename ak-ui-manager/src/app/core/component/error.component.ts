import { Component } from '@angular/core';

@Component({
    template: `
    <div style="min-height: 800px; height: 80%;" type="404" class="exception">
        <div class="exception__img-block">
            <div class="exception__img"></div>
        </div>
        <div class="exception__cont">
            <h1 class="exception__cont-title">404</h1>
            <div class="exception__cont-desc">抱歉，你访问的页面不存在</div>
            <div class="exception__cont-actions">
                <!--<button nz-button nzType="default" (click)="toolService.goBack()" style="margin-right: 60px;">返回</button>-->
            </div>
        </div>
    </div>
    `,
    styles: [`
        .exception {
            display: flex;
            align-items: center;
            height: 100%;
        }

        .exception__img-block {
            flex: 0 0 62.5%;
            width: 62.5%;
        }

        .exception__img {
            width: 700px;
            height: 700px;
            background-image: url('../../../assets/images/404.png');
            max-width: 700px;
            float: right;
            background-repeat: no-repeat;
            background-position: 50% 50%;
            background-size: 100% 100%;
        }

        .exception__cont-title {
            color: #434e59;
            font-size: 72px;
            font-weight: 600;
            line-height: 72px;
            margin-bottom: 24px;
        }

        .exception__cont-desc {
            color: rgba(0, 0, 0, .45);
            font-size: 20px;
            line-height: 28px;
            margin-bottom: 16px;
        }
    `]
})
export class ErrorComponent {
    constructor() { }
}
