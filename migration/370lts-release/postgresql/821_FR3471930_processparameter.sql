-- 10-gen-2012 18.38.29 CET
-- FR3471930 - Alllow consolidation of PO from Req. with different dates
INSERT INTO AD_Element (AD_Client_ID,AD_Element_ID,AD_Org_ID,ColumnName,Created,CreatedBy,Description,EntityType,IsActive,Name,PrintName,Updated,UpdatedBy) VALUES (0,55368,0,'ConsolidateByDatePromised',TO_TIMESTAMP('2012-01-10 18:38:28','YYYY-MM-DD HH24:MI:SS'),100,'Consolidate into one document if date promised are equals','D','Y','Consolidate by date promised','Consolidate by date promised',TO_TIMESTAMP('2012-01-10 18:38:28','YYYY-MM-DD HH24:MI:SS'),100)
;

-- 10-gen-2012 18.38.29 CET
-- FR3471930 - Alllow consolidation of PO from Req. with different dates
INSERT INTO AD_Element_Trl (AD_Language,AD_Element_ID, Description,Help,Name,PO_Description,PO_Help,PO_Name,PO_PrintName,PrintName, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy) SELECT l.AD_Language,t.AD_Element_ID, t.Description,t.Help,t.Name,t.PO_Description,t.PO_Help,t.PO_Name,t.PO_PrintName,t.PrintName, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy FROM AD_Language l, AD_Element t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Element_ID=55368 AND NOT EXISTS (SELECT * FROM AD_Element_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Element_ID=t.AD_Element_ID)
;

-- 10-gen-2012 18.39.19 CET
-- FR3471930 - Alllow consolidation of PO from Req. with different dates
UPDATE AD_Element_Trl SET Name='Consolida per data promessa',PrintName='Consolida per data promessa',Description='Consolida in un unico documento se la data promessa e'' uguale',Updated=TO_TIMESTAMP('2012-01-10 18:39:19','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Element_ID=55368 AND AD_Language='it_IT'
;

-- 10-gen-2012 18.47.38 CET
-- FR3471930 - Alllow consolidation of PO from Req. with different dates
INSERT INTO AD_Process_Para (AD_Client_ID,AD_Element_ID,AD_Org_ID,AD_Process_ID,AD_Process_Para_ID,AD_Reference_ID,ColumnName,Created,CreatedBy,DefaultValue,Description,DisplayLogic,EntityType,FieldLength,IsActive,IsCentrallyMaintained,IsMandatory,IsRange,Name,SeqNo,Updated,UpdatedBy) VALUES (0,55368,0,337,53598,20,'ConsolidateByDatePromised',TO_TIMESTAMP('2012-01-10 18:47:37','YYYY-MM-DD HH24:MI:SS'),100,'Y','Consolidate into one document if date promised are equals','@ConsolidateDocument@=Y','D',0,'Y','Y','Y','N','Consolidate by date promised',120,TO_TIMESTAMP('2012-01-10 18:47:37','YYYY-MM-DD HH24:MI:SS'),100)
;

-- 10-gen-2012 18.47.38 CET
-- FR3471930 - Alllow consolidation of PO from Req. with different dates
INSERT INTO AD_Process_Para_Trl (AD_Language,AD_Process_Para_ID, Description,Help,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy) SELECT l.AD_Language,t.AD_Process_Para_ID, t.Description,t.Help,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy FROM AD_Language l, AD_Process_Para t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Process_Para_ID=53598 AND NOT EXISTS (SELECT * FROM AD_Process_Para_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Process_Para_ID=t.AD_Process_Para_ID)
;

-- 10-gen-2012 18.50.14 CET
-- FR3471930 - Alllow consolidation of PO from Req. with different dates
UPDATE AD_Process_Para_Trl SET Name='Consolida per data promessa',Description='Consolida in un unico documento se la data promessa e'' uguale',Updated=TO_TIMESTAMP('2012-01-10 18:50:14','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Process_Para_ID=53598 AND AD_Language='it_IT'
;

