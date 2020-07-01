import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DemoSharedModule } from 'app/shared/shared.module';
import { ResidentComponent } from './resident.component';
import { ResidentDetailComponent } from './resident-detail.component';
import { ResidentUpdateComponent } from './resident-update.component';
import { ResidentDeleteDialogComponent } from './resident-delete-dialog.component';
import { residentRoute } from './resident.route';

@NgModule({
  imports: [DemoSharedModule, RouterModule.forChild(residentRoute)],
  declarations: [ResidentComponent, ResidentDetailComponent, ResidentUpdateComponent, ResidentDeleteDialogComponent],
  entryComponents: [ResidentDeleteDialogComponent],
})
export class DemoResidentModule {}
