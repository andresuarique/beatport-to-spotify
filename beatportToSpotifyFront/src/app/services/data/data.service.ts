import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private data = new BehaviorSubject<any>('jhon');
  dataRead = this.data.asObservable();

  editData(newData:any){
    this.data.next(newData);
  }
}
