import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { BankAccount } from './bank-account.model';
import { DateUtils } from 'ng-jhipster';

@Injectable()
export class BankAccountService {

    private resourceUrl = 'api/bank-accounts';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(bankAccount: BankAccount): Observable<BankAccount> {
        const copy: BankAccount = Object.assign({}, bankAccount);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(bankAccount: BankAccount): Observable<BankAccount> {
        const copy: BankAccount = Object.assign({}, bankAccount);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<BankAccount> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    importLines(id: number, file: File): Observable<Response> {
        const formData = new FormData();
        formData.append('file', file, file.name);
        return this.http.post(`${this.resourceUrl}/${id}/import-lines`, formData);
    }

    generateLines(id: number, date: Date): Observable<Response> {
        const options: BaseRequestOptions = new BaseRequestOptions();
        const params: URLSearchParams = new URLSearchParams();
        params.set('date', this.dateUtils.convertLocalDateToServer(date));
        options.search = params;
        return this.http.post(`${this.resourceUrl}/${id}/generate-lines`, null, options);
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }
}
