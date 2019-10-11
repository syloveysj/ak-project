import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {CredentialInterceptor} from "@service/interceptor/credential-interceptor";

// 在这里添加需要的拦截器，就不需要再修改 AppModule。
export const httpInterceptorProviders = [
    {provide: HTTP_INTERCEPTORS, useClass: CredentialInterceptor, multi: true}
];
