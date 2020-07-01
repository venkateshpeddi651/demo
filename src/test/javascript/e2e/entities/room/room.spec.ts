import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  RoomComponentsPage,
  /* RoomDeleteDialog, */
  RoomUpdatePage,
} from './room.page-object';

const expect = chai.expect;

describe('Room e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let roomComponentsPage: RoomComponentsPage;
  let roomUpdatePage: RoomUpdatePage;
  /* let roomDeleteDialog: RoomDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Rooms', async () => {
    await navBarPage.goToEntity('room');
    roomComponentsPage = new RoomComponentsPage();
    await browser.wait(ec.visibilityOf(roomComponentsPage.title), 5000);
    expect(await roomComponentsPage.getTitle()).to.eq('Rooms');
    await browser.wait(ec.or(ec.visibilityOf(roomComponentsPage.entities), ec.visibilityOf(roomComponentsPage.noResult)), 1000);
  });

  it('should load create Room page', async () => {
    await roomComponentsPage.clickOnCreateButton();
    roomUpdatePage = new RoomUpdatePage();
    expect(await roomUpdatePage.getPageTitle()).to.eq('Create or edit a Room');
    await roomUpdatePage.cancel();
  });

  /* it('should create and save Rooms', async () => {
        const nbButtonsBeforeCreate = await roomComponentsPage.countDeleteButtons();

        await roomComponentsPage.clickOnCreateButton();

        await promise.all([
            roomUpdatePage.setRoomNumberInput('roomNumber'),
            roomUpdatePage.setNameInput('name'),
            roomUpdatePage.setCreatedByInput('createdBy'),
            roomUpdatePage.setCreatedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            roomUpdatePage.setLastModifiedByInput('lastModifiedBy'),
            roomUpdatePage.setLastModifiedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            roomUpdatePage.facilitySelectLastOption(),
        ]);

        expect(await roomUpdatePage.getRoomNumberInput()).to.eq('roomNumber', 'Expected RoomNumber value to be equals to roomNumber');
        expect(await roomUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
        expect(await roomUpdatePage.getCreatedByInput()).to.eq('createdBy', 'Expected CreatedBy value to be equals to createdBy');
        expect(await roomUpdatePage.getCreatedDateInput()).to.contain('2001-01-01T02:30', 'Expected createdDate value to be equals to 2000-12-31');
        expect(await roomUpdatePage.getLastModifiedByInput()).to.eq('lastModifiedBy', 'Expected LastModifiedBy value to be equals to lastModifiedBy');
        expect(await roomUpdatePage.getLastModifiedDateInput()).to.contain('2001-01-01T02:30', 'Expected lastModifiedDate value to be equals to 2000-12-31');

        await roomUpdatePage.save();
        expect(await roomUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await roomComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Room', async () => {
        const nbButtonsBeforeDelete = await roomComponentsPage.countDeleteButtons();
        await roomComponentsPage.clickOnLastDeleteButton();

        roomDeleteDialog = new RoomDeleteDialog();
        expect(await roomDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Room?');
        await roomDeleteDialog.clickOnConfirmButton();

        expect(await roomComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
