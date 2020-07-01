import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IResident } from 'app/shared/model/resident.model';

type EntityResponseType = HttpResponse<IResident>;
type EntityArrayResponseType = HttpResponse<IResident[]>;

@Injectable({ providedIn: 'root' })
export class ResidentService {
  public resourceUrl = SERVER_API_URL + 'api/residents';

  constructor(protected http: HttpClient) {}

  create(resident: IResident): Observable<EntityResponseType> {
    return this.http.post<IResident>(this.resourceUrl, resident, { observe: 'response' });
  }

  update(resident: IResident): Observable<EntityResponseType> {
    return this.http.put<IResident>(this.resourceUrl, resident, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResident>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResident[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
  
  unassignedUsers(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResident[]>(this.resourceUrl + '/unassigned', { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
