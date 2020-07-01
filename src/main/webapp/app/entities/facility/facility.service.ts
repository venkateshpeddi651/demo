import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFacility } from 'app/shared/model/facility.model';

type EntityResponseType = HttpResponse<IFacility>;
type EntityArrayResponseType = HttpResponse<IFacility[]>;

@Injectable({ providedIn: 'root' })
export class FacilityService {
  public resourceUrl = SERVER_API_URL + 'api/facilities';

  constructor(protected http: HttpClient) {}

  create(facility: IFacility): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facility);
    return this.http
      .post<IFacility>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(facility: IFacility): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facility);
    return this.http
      .put<IFacility>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFacility>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFacility[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(facility: IFacility): IFacility {
    const copy: IFacility = Object.assign({}, facility, {
      createdDate: facility.createdDate && facility.createdDate.isValid() ? facility.createdDate.toJSON() : undefined,
      lastModifiedDate: facility.lastModifiedDate && facility.lastModifiedDate.isValid() ? facility.lastModifiedDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? moment(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((facility: IFacility) => {
        facility.createdDate = facility.createdDate ? moment(facility.createdDate) : undefined;
        facility.lastModifiedDate = facility.lastModifiedDate ? moment(facility.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
