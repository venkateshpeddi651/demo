import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRoom } from 'app/shared/model/room.model';

type EntityResponseType = HttpResponse<IRoom>;
type EntityArrayResponseType = HttpResponse<IRoom[]>;

@Injectable({ providedIn: 'root' })
export class RoomService {
  public resourceUrl = SERVER_API_URL + 'api/rooms';

  constructor(protected http: HttpClient) {}

  create(room: IRoom): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(room);
    return this.http
      .post<IRoom>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(room: IRoom): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(room);
    return this.http
      .put<IRoom>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRoom>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRoom[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(room: IRoom): IRoom {
    const copy: IRoom = Object.assign({}, room, {
      createdDate: room.createdDate && room.createdDate.isValid() ? room.createdDate.toJSON() : undefined,
      lastModifiedDate: room.lastModifiedDate && room.lastModifiedDate.isValid() ? room.lastModifiedDate.toJSON() : undefined,
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
      res.body.forEach((room: IRoom) => {
        room.createdDate = room.createdDate ? moment(room.createdDate) : undefined;
        room.lastModifiedDate = room.lastModifiedDate ? moment(room.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
