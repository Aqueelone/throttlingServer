export const enum TicketStatus {
  'PENDING',
  'REJECTED',
  'OPEN',
  'SOLVED',
  'CANCELED'
}

export interface ITicket {
  id?: number;
  description?: string;
  status?: TicketStatus;
  userLogin?: string;
  userId?: number;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public description?: string,
    public status?: TicketStatus,
    public userLogin?: string,
    public userId?: number
  ) {}
}
