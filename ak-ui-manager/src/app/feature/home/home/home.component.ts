import {AfterViewInit, Component, OnInit, Renderer2} from '@angular/core';
import * as joint from 'jointjs/dist/joint.js';
import {BaseComponent} from "@shared/base-class/base.component";
import {BaseService} from "@service/http/base.service";
import {NzMessageService, NzModalService} from "ng-zorro-antd";
import {Config} from "@config/config";

declare var $:JQueryStatic;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent extends BaseComponent implements OnInit, AfterViewInit {

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

  private graph: any;
  private paper: any;

  ngOnInit() {
  }

  ngAfterViewInit() {
      setTimeout(() => {
          this.initFlow();
      }, 100);
  }

  getHeight(): string {
      let height: number = Number(this.scrollY.replace('px', ''));
      return (height + 170) + 'px';
  }

  initFlow(): void {
      const w = $("#flowView").width();	//content1为div
      const h = $("#flowView").height();
      this.graph = new joint.dia.Graph;
      this.paper = new joint.dia.Paper({
          el: $("#flowView"),
          width: w,
          height: h,
          model: this.graph,
          drawGrid: false,
          background: {
              color: 'rgb(240,255,255)'
          }
      });
      let rect = new joint.shapes.basic.Rect({
          position: { x: 100, y: 30 },
          size: { width: 100, height: 30 },
          attrs: { rect: { fill: 'blue' }, text: { text: 'my box', fill: 'white' } }
      });
      let rect2 = rect.clone() as joint.shapes.basic.Rect;
      rect2.translate(300);
      const link = new joint.dia.Link({
          source: { id: rect.id },
          target: { id: rect2.id }
      });
      this.graph.addCells([rect, rect2, link]);
  }

}
