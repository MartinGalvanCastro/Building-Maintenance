# MaintenanceRequestDto


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **string** |  | [optional] [default to undefined]
**resident** | [**ResidentSummaryDto**](ResidentSummaryDto.md) |  | [optional] [default to undefined]
**description** | **string** |  | [optional] [default to undefined]
**specialization** | **string** |  | [optional] [default to undefined]
**status** | **string** |  | [optional] [default to undefined]
**createdAt** | **string** |  | [optional] [default to undefined]
**scheduledAt** | **string** |  | [optional] [default to undefined]
**completedAt** | **string** |  | [optional] [default to undefined]
**technician** | [**TechnicianSummaryDto**](TechnicianSummaryDto.md) |  | [optional] [default to undefined]

## Example

```typescript
import { MaintenanceRequestDto } from './api';

const instance: MaintenanceRequestDto = {
    id,
    resident,
    description,
    specialization,
    status,
    createdAt,
    scheduledAt,
    completedAt,
    technician,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
