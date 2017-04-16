import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Line, LineStatus, LineSource } from './line.model';
import { DateUtils } from 'ng-jhipster';
@Injectable()
export class LineService {

    private resourceUrl = 'api/lines';
    private bankAccountResourceUrl = 'api/bank-accounts/:bankAccountId/lines';
    private resourceSearchUrl = 'api/_search/lines';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(line: Line): Observable<Line> {
        let copy: Line = Object.assign({}, line);
        copy.date = this.dateUtils
            .convertLocalDateToServer(line.date);
        copy.createDate = this.dateUtils.toDate(line.createDate);

        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(line: Line): Observable<Line> {
        let copy: Line = Object.assign({}, line);
        copy.date = this.dateUtils
            .convertLocalDateToServer(line.date);
        copy.createDate = this.dateUtils.toDate(line.createDate);

        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Line> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.date = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.date);
            jsonResponse.createDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.createDate);
            jsonResponse.effectiveDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.effectiveDate);

            return jsonResponse;
        });
    }

    findByBankAccount(bankAccountId: number, req?: any): Observable<Line> {
        let options = this.createRequestOption(req);
        return this.http.get(this.bankAccountResourceUrl.replace(/:bankAccountId/, bankAccountId.toString()), options);
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    reIndex(): Observable<Response> {
        return this.http.get(`${this.resourceUrl}/reindex`);
    }

    search(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res))
            ;
    }

    createNew(bankAccountId: number, generated: boolean) {
        let line = new Line();
        if (bankAccountId) { line.bankAccountId = Number(bankAccountId); }
        generated ? line.source = LineSource.GENERATED : line.source = LineSource.MANUAL;
        line.status = LineStatus.NEW;
        return line;
    }

    tickLine(line: Line): Observable<Line> {
        let copy: Line = Object.assign({}, line);
        return this.http.put(`${this.resourceUrl}/${line.id}/tick`, null).map((res: Response) => {
            return res.json();
        });
    }

    cancelLine(line: Line): Observable<Line> {
        let copy: Line = Object.assign({}, line);
        return this.http.put(`${this.resourceUrl}/${line.id}/cancel`, null).map((res: Response) => {
            return res.json();
        });
    }

    private convertResponse(res: any): any {
        let jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].date = this.dateUtils
                .convertLocalDateFromServer(jsonResponse[i].date);
            jsonResponse[i].createDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].createDate);
            jsonResponse[i].effectiveDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse[i].effectiveDate);
        }
        res._body = jsonResponse;
        return res;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
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
