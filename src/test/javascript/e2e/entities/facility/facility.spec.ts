import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  FacilityComponentsPage,
  /* FacilityDeleteDialog, */
  FacilityUpdatePage,
} from './facility.page-object';

const expect = chai.expect;

describe('Facility e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let facilityComponentsPage: FacilityComponentsPage;
  let facilityUpdatePage: FacilityUpdatePage;
  /* let facilityDeleteDialog: FacilityDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Facilities', async () => {
    await navBarPage.goToEntity('facility');
    facilityComponentsPage = new FacilityComponentsPage();
    await browser.wait(ec.visibilityOf(facilityComponentsPage.title), 5000);
    expect(await facilityComponentsPage.getTitle()).to.eq('Facilities');
    await browser.wait(ec.or(ec.visibilityOf(facilityComponentsPage.entities), ec.visibilityOf(facilityComponentsPage.noResult)), 1000);
  });

  it('should load create Facility page', async () => {
    await facilityComponentsPage.clickOnCreateButton();
    facilityUpdatePage = new FacilityUpdatePage();
    expect(await facilityUpdatePage.getPageTitle()).to.eq('Create or edit a Facility');
    await facilityUpdatePage.cancel();
  });

  /* it('should create and save Facilities', async () => {
        const nbButtonsBeforeCreate = await facilityComponentsPage.countDeleteButtons();

        await facilityComponentsPage.clickOnCreateButton();

        await promise.all([
            facilityUpdatePage.setNameInput('name'),
            facilityUpdatePage.setCreatedByInput('createdBy'),
            facilityUpdatePage.setCreatedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            facilityUpdatePage.setLastModifiedByInput('lastModifiedBy'),
            facilityUpdatePage.setLastModifiedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            facilityUpdatePage.facilityAdminSelectLastOption(),
        ]);

        expect(await facilityUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
        expect(await facilityUpdatePage.getCreatedByInput()).to.eq('createdBy', 'Expected CreatedBy value to be equals to createdBy');
        expect(await facilityUpdatePage.getCreatedDateInput()).to.contain('2001-01-01T02:30', 'Expected createdDate value to be equals to 2000-12-31');
        expect(await facilityUpdatePage.getLastModifiedByInput()).to.eq('lastModifiedBy', 'Expected LastModifiedBy value to be equals to lastModifiedBy');
        expect(await facilityUpdatePage.getLastModifiedDateInput()).to.contain('2001-01-01T02:30', 'Expected lastModifiedDate value to be equals to 2000-12-31');

        await facilityUpdatePage.save();
        expect(await facilityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await facilityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Facility', async () => {
        const nbButtonsBeforeDelete = await facilityComponentsPage.countDeleteButtons();
        await facilityComponentsPage.clickOnLastDeleteButton();

        facilityDeleteDialog = new FacilityDeleteDialog();
        expect(await facilityDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Facility?');
        await facilityDeleteDialog.clickOnConfirmButton();

        expect(await facilityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
