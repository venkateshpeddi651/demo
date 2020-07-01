import { Moment } from 'moment';

export interface IRoom {
  id?: number;
  roomNumber?: string;
  name?: string;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  facilityId?: number;
}

export class Room implements IRoom {
  constructor(
    public id?: number,
    public roomNumber?: string,
    public name?: string,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public facilityId?: number
  ) {}
}
