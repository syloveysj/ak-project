export abstract class Config {
    public auth: {addr: string, clientId: string, secret: string};
    public apiAddr: string;
    public menus: any[];
}
