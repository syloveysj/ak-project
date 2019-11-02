import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Config} from '@config/config';
import {Router} from "@angular/router";
import {Cookie} from "ng2-cookies";

@Injectable({
    providedIn: 'root',
})
export class Oauth2Service {

    constructor(private config: Config,
                private _router: Router,
                private _http: HttpClient){
    }

    obtainAccessToken(loginData){
        const params = new URLSearchParams();
        params.append('username',loginData.username);
        params.append('password',loginData.password);
        params.append('grant_type','password');
        params.append('client_id',this.config.auth.clientId);

        const headers = new HttpHeaders({
            'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
            'Authorization': 'Basic ' + btoa(this.config.auth.clientId + ":" + this.config.auth.secret)});
        const options = { headers: headers };
        this._http.post(this.config.auth.addr, params.toString(), options)
            .subscribe( data => {
                    this.saveToken(data);
                }
            );
    }

    saveToken(token){
        const expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + (token.expires_in * 1000));
        Cookie.set("access_token", token.value, expireDate, '/');
        this._router.navigate(['/index']);
    }

    getResource(resourceUrl) : Observable<any>{
        return this._http.get(resourceUrl);
    }

    checkCredentials(){
        if (!Cookie.check('access_token')){
            this._router.navigate(['/auth/login']);
        }
    }

    logout() {
        Cookie.delete('access_token', '/');
        this._router.navigate(['/auth/login']);
    }

}
