import { EngineType } from 'app/shared/model/enumerations/engine-type.model';
import { DrivetrainType } from 'app/shared/model/enumerations/drivetrain-type.model';
import { TransmissionType } from 'app/shared/model/enumerations/transmission-type.model';
import { Trim } from 'app/shared/model/enumerations/trim.model';
import { Color } from 'app/shared/model/enumerations/color.model';

export interface IVehicleOptions {
  id?: number;
  estimatedMileage?: number | null;
  engine?: keyof typeof EngineType | null;
  drivetrain?: keyof typeof DrivetrainType | null;
  transmission?: keyof typeof TransmissionType | null;
  trim?: keyof typeof Trim | null;
  color?: keyof typeof Color | null;
}

export const defaultValue: Readonly<IVehicleOptions> = {};
