import {Component} from '@angular/core';

@Component({
    selector: 'app-task',
    template: `
        <router-outlet></router-outlet>
    `
})
export class TaskComponent {
    public constructor() {
    }
}
