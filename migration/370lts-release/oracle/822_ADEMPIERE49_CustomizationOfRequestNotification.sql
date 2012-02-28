-- 20-feb-2012 17.53.05 CET
-- ADEMPIERE-49 Customization of mail sent by request notifications
INSERT INTO AD_Column (AD_Client_ID,AD_Column_ID,AD_Element_ID,AD_Org_ID,AD_Reference_ID,AD_Table_ID,ColumnName,Created,CreatedBy,DefaultValue,Description,EntityType,FieldLength,Help,IsActive,IsAllowLogging,IsAlwaysUpdateable,IsAutocomplete,IsEncrypted,IsIdentifier,IsKey,IsMandatory,IsParent,IsSelectionColumn,IsSyncDatabase,IsTranslated,IsUpdateable,MandatoryLogic,Name,SeqNo,Updated,UpdatedBy,Version) VALUES (0,62783,1515,0,19,529,'R_MailText_ID',TO_DATE('2012-02-20 17:53:04','YYYY-MM-DD HH24:MI:SS'),100,NULL,'Text templates for mailings','D',10,'The Mail Template indicates the mail template for return messages. Mail text can include variables.  The priority of parsing is User/Contact, Business Partner and then the underlying business object (like Request, Dunning, Workflow object).<br>
So, @Name@ would resolve into the User name (if user is defined defined), then Business Partner name (if business partner is defined) and then the Name of the business object if it has a Name.<br>
For Multi-Lingual systems, the template is translated based on the Business Partner''s language selection.','Y','Y','N','N','N','N','N','N','N','N','N','N','Y',NULL,'Mail Template',0,TO_DATE('2012-02-20 17:53:04','YYYY-MM-DD HH24:MI:SS'),100,0)
;

-- 20-feb-2012 17.53.05 CET
-- ADEMPIERE-49 Customization of mail sent by request notifications
INSERT INTO AD_Column_Trl (AD_Language,AD_Column_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy) SELECT l.AD_Language,t.AD_Column_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy FROM AD_Language l, AD_Column t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Column_ID=62783 AND NOT EXISTS (SELECT * FROM AD_Column_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Column_ID=t.AD_Column_ID)
;

-- 20-feb-2012 17.53.15 CET
-- ADEMPIERE-49 Customization of mail sent by request notifications
ALTER TABLE R_RequestType ADD R_MailText_ID NUMBER(10) DEFAULT NULL 
;

-- 20-feb-2012 17.53.15 CET
-- ADEMPIERE-49 Customization of mail sent by request notifications
ALTER TABLE R_RequestType ADD CONSTRAINT RMAILTEXT_RREQUESTTYPE
	FOREIGN KEY (R_MailText_ID) REFERENCES R_MailText(R_MailText_ID)
;

-- 20-feb-2012 17.56.40 CET
-- ADEMPIERE-49 Customization of mail sent by request notifications
INSERT INTO AD_Field (AD_Client_ID,AD_Column_ID,AD_Field_ID,AD_Org_ID,AD_Tab_ID,Created,CreatedBy,Description,DisplayLength,EntityType,Help,IsActive,IsCentrallyMaintained,IsDisplayed,IsEncrypted,IsFieldOnly,IsHeading,IsReadOnly,IsSameLine,Name,Updated,UpdatedBy) VALUES (0,62783,64062,0,437,TO_DATE('2012-02-20 17:56:40','YYYY-MM-DD HH24:MI:SS'),100,'Text templates for mailings',10,'D','The Mail Template indicates the mail template for return messages. Mail text can include variables.  The priority of parsing is User/Contact, Business Partner and then the underlying business object (like Request, Dunning, Workflow object).<br>
So, @Name@ would resolve into the User name (if user is defined defined), then Business Partner name (if business partner is defined) and then the Name of the business object if it has a Name.<br>
For Multi-Lingual systems, the template is translated based on the Business Partner''s language selection.','Y','Y','Y','N','N','N','N','N','Mail Template',TO_DATE('2012-02-20 17:56:40','YYYY-MM-DD HH24:MI:SS'),100)
;

-- 20-feb-2012 17.56.40 CET
-- ADEMPIERE-49 Customization of mail sent by request notifications
INSERT INTO AD_Field_Trl (AD_Language,AD_Field_ID, Description,Help,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy) SELECT l.AD_Language,t.AD_Field_ID, t.Description,t.Help,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy FROM AD_Language l, AD_Field t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Field_ID=64062 AND NOT EXISTS (SELECT * FROM AD_Field_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Field_ID=t.AD_Field_ID)
;

-- 20-feb-2012 17.56.54 CET
-- ADEMPIERE-49 Customization of mail sent by request notifications
UPDATE AD_Field SET SeqNo=170,Updated=TO_DATE('2012-02-20 17:56:54','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=64062
;

