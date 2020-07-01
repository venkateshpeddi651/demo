import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFacility } from 'app/shared/model/facility.model';
import { FacilityService } from './facility.service';

@Component({
  templateUrl: './facility-delete-dialog.component.html',
})
export class FacilityDeleteDialogComponent {
  facility?: IFacility;

  constructor(protected facilityService: FacilityService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.facilityService.delete(id).subscribe(() => {
      this.eventManager.broadcast('facilityListModification');
      this.activeModal.close();
    });
  }
}
