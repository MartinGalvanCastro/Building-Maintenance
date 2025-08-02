# CreateMaintenanceRequestDto

Payload for creating a new maintenance request. Resident ID is required.

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**residentId** | **string** | ID of the resident making the request | [default to undefined]
**description** | **string** | Description of the maintenance issue | [default to undefined]
**specialization** | **string** | Required technician specialization | [default to undefined]
**scheduledAt** | **string** | Scheduled date and time for the maintenance | [default to undefined]

## Example

```typescript
import { CreateMaintenanceRequestDto } from './api';

const instance: CreateMaintenanceRequestDto = {
    residentId,
    description,
    specialization,
    scheduledAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
