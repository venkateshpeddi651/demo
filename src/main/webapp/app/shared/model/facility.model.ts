import { Moment } from 'moment';

export interface IFacility {
  id?: number;
  name?: string;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  facilityAdminLogin?: string;
  facilityAdminId?: number;
}

export class Facility implements IFacility {
  constructor(
    public id?: number,
    public name?: string,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public facilityAdminLogin?: string,
    public facilityAdminId?: number
  ) {}
}
