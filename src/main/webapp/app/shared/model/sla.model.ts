export interface ISLA {
  id?: number;
  rps?: number;
  userLogin?: string;
  userId?: number;
}

export class SLA implements ISLA {
  constructor(public id?: number, public rps?: number, public userLogin?: string, public userId?: number) {}
}
