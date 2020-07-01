import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IResident } from 'app/shared/model/resident.model';
import { ResidentService } from './resident.service';

@Component({
  templateUrl: './resident-delete-dialog.component.html',
})
export class ResidentDeleteDialogComponent {
  resident?: IResident;

  constructor(protected residentService: ResidentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.residentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('residentListModification');
      this.activeModal.close();
    });
  }
}
