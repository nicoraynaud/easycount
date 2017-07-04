import {Component, OnInit} from '@angular/core';
import {VERSION} from '../../app.constants';

@Component({
    selector: 'jhi-footer',
    templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {

    version: string;

    ngOnInit(): void {
        this.version = VERSION ? 'v' + VERSION : '';
    }

}
