import { element, by, ElementFinder } from 'protractor';

export class FacilityComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-facility div table .btn-danger'));
  title = element.all(by.css('jhi-facility div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class FacilityUpdatePage {
  pageTitle = element(by.id('jhi-facility-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));
  createdByInput = element(by.id('field_createdBy'));
  createdDateInput = element(by.id('field_createdDate'));
  lastModifiedByInput = element(by.id('field_lastModifiedBy'));
  lastModifiedDateInput = element(by.id('field_lastModifiedDate'));

  facilityAdminSelect = element(by.id('field_facilityAdmin'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setCreatedByInput(createdBy: string): Promise<void> {
    await this.createdByInput.sendKeys(createdBy);
  }

  async getCreatedByInput(): Promise<string> {
    return await this.createdByInput.getAttribute('value');
  }

  async setCreatedDateInput(createdDate: string): Promise<void> {
    await this.createdDateInput.sendKeys(createdDate);
  }

  async getCreatedDateInput(): Promise<string> {
    return await this.createdDateInput.getAttribute('value');
  }

  async setLastModifiedByInput(lastModifiedBy: string): Promise<void> {
    await this.lastModifiedByInput.sendKeys(lastModifiedBy);
  }

  async getLastModifiedByInput(): Promise<string> {
    return await this.lastModifiedByInput.getAttribute('value');
  }

  async setLastModifiedDateInput(lastModifiedDate: string): Promise<void> {
    await this.lastModifiedDateInput.sendKeys(lastModifiedDate);
  }

  async getLastModifiedDateInput(): Promise<string> {
    return await this.lastModifiedDateInput.getAttribute('value');
  }

  async facilityAdminSelectLastOption(): Promise<void> {
    await this.facilityAdminSelect.all(by.tagName('option')).last().click();
  }

  async facilityAdminSelectOption(option: string): Promise<void> {
    await this.facilityAdminSelect.sendKeys(option);
  }

  getFacilityAdminSelect(): ElementFinder {
    return this.facilityAdminSelect;
  }

  async getFacilityAdminSelectedOption(): Promise<string> {
    return await this.facilityAdminSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class FacilityDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-facility-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-facility'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
