export interface IUser {
  id?: number;
  login?: string;
}

export class User implements IUser {
  constructor(public id: number, public login: string) {}
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}
export function getUserLogin(user: IUser): string | undefined{
  return  user.login;
}
