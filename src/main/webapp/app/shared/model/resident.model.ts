export interface IResident {
  id?: number;
  phone?: string;
  roomId?: number;
  userLogin?: string;
  userId?: number;
}

export class Resident implements IResident {
  constructor(public id?: number, public phone?: string, public roomId?: number, public userLogin?: string, public userId?: number) {}
}
